package PACKAGE_NAME;

public class MyHashMap<K, V> {
    // "Ведра" - массив, где хранятся наши данные. Каждый элемент - это связный список
    private Node<K, V>[] buckets;
    // Сколько у нас ведер изначально (можно потом увеличивать)
    private int capacity = 16;

    // Внутренний класс для хранения пары ключ-значение
    // Работает как элемент связного списка (отсюда и next)
    private static class Node<K, V> {
        K key;           // То, по чему ищем
        V value;         // То, что храним
        Node<K, V> next; // Ссылка на следующий элемент в цепочке

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        // Создаем массив с нужным количеством ведер
        buckets = new Node[capacity];
    }

    // Решаем, в какое ведро положить пару ключ-значение
    private int getBucketIndex(K key) {
        // Для null ключа всегда первое ведро
        if (key == null) return 0;
        // Для остальных - берем хеш-код и "причесываем" его под размер массива
        return Math.abs(key.hashCode()) % capacity;
    }

    // Кладем значение в мапу (или обновляем, если ключ уже есть)
    public void put(K key, V value) {
        // Определяем, в каком ведре должен лежать ключ
        int index = getBucketIndex(key);
        // Берем первый элемент в этом ведре
        Node<K, V> current = buckets[index];

        // Проходим по всей цепочке в поисках нашего ключа
        while (current != null) {
            // Сравниваем ключи (аккуратно, с проверкой на null)
            if (current.key != null && current.key.equals(key)) {
                // Нашли - просто обновляем значение и уходим
                current.value = value;
                return;
            }
            current = current.next;
        }

        // Если ключ не нашли - добавляем новую пару в начало списка
        Node<K, V> newNode = new Node<>(key, value);
        newNode.next = buckets[index];  // Новый элемент ссылается на старый первый
        buckets[index] = newNode;       // Теперь новый элемент - первый в списке
    }

    // Забираем значение по ключу
    public V get(K key) {
        // Вычисляем нужное ведро
        int index = getBucketIndex(key);
        // Начинаем просмотр с первого элемента в этом ведре
        Node<K, V> current = buckets[index];

        // Бежим по цепочке, пока не найдем нужный ключ
        while (current != null) {
            // Проверяем два случая: оба ключа null или равные не-null ключи
            if ((current.key == null && key == null) ||
                    (current.key != null && current.key.equals(key))) {
                return current.value;  // Ура, нашли!
            }
            current = current.next;
        }
        return null; // Ничего не нашли
    }

    // Удаляем пару ключ-значение
    public void remove(K key) {
        // Находим нужное ведро
        int index = getBucketIndex(key);
        Node<K, V> current = buckets[index];
        Node<K, V> previous = null;  // Запоминаем предыдущий элемент (для перестройки связей)

        // Ищем элемент с нужным ключом
        while (current != null) {
            if ((current.key == null && key == null) ||
                    (current.key != null && current.key.equals(key))) {
                // Нашли! Теперь нужно "вырезать" его из списка
                if (previous != null) {
                    // Если элемент не первый в списке - просто перекидываем ссылку
                    previous.next = current.next;
                } else {
                    // Если элемент первый - теперь первым становится следующий
                    buckets[index] = current.next;
                }
                return; // Удалили и уходим
            }
            // Идем дальше
            previous = current;
            current = current.next;
        }
        // Если ключ не нашли - ничего не делаем
    }
}

package com.video;

class Node<K, V> {
    K key;
    V value;
    int hashcode;
    Node<K, V> next;

    Node(K key, V value, int hashcode, Node<K, V> next) {
        this.key = key;
        this.value = value;
        this.hashcode = hashcode;
        this.next = next;
    }
}

class Hashmap<K, V> {
    int size = 0;
    Double threshold = 0.75;
    Node<K, V>[] nodes;

    int getHash(K key) {
        int originHash = key.hashCode();
        return originHash ^ (originHash >> 16);
    }

    int getIndex(int hash) {
        return (nodes.length - 1) & hash;
    }

    V put(K key, V value) {
        int hash = getHash(key);
        int i = getIndex(hash);
        if (nodes[i] == null) {
            nodes[i] = new Node<>(key, value, hash, null);
            size++;
            return value;
        } else {
            //如果nodes[i]是红黑树，走红黑树的插入
            //如果nodes[i]是链表结点
            Node<K, V> p = nodes[i];
            Node<K, V> pre = null;
            while (p != null) {
                //如果有一样的结点，覆盖
                if (p.hashcode == hash && (p.key == key || p.key.equals(key))) {
                    V old = p.value;
                    p.value = value;
                    return old;
                }
                pre = p;
                p = p.next;
            }
            //如果没有一样的结点，尾插
            pre.next = new Node<>(key, value, hash, null);
        }

        if (size > nodes.length * threshold) {
            System.out.println("扩容");
        }
        return null;
    }

    V get(K key) {
        int hash = getHash(key);
        int i = getIndex(hash);
        Node<K, V> p = nodes[i];
        while (p != null) {
            if (p.hashcode == hash && (p.key == key || p.key.equals(key))) {
                return p.value;
            }
            p = p.next;
        }
        return null;
    }
}

public class TestHashmap {
    public static void main(String[] args) {
    }
}

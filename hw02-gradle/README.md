Реализация Задания:<p>
Написать свою реализацию ArrayList на основе массива.<br>
class DIYarrayList<T> implements List<T>{...}<p>
Проверить, что на ней работают методы из java.util.Collections:<br>
Collections.addAll(Collection<? super T> c, T... elements)<br>
Collections.static <T> void copy(List<? super T> dest, List<? extends T> src)<br>
Collections.static <T> void sort(List<T> list, Comparator<? super T> c)<br>

1) Проверяйте на коллекциях с 20 и больше элементами.
2) DIYarrayList должен имплементировать ТОЛЬКО ОДИН интерфейс - List.
3) Если метод не имплементирован, то он должен выбрасывать исключение UnsupportedOperationException.
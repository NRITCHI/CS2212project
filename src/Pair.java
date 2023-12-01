/**
 * Stores 2 values for easier access
 * @param <X> type of first value
 * @param <Y> type of second value
 */
public class Pair<X, Y> {
    /**
     * first value
     */
    public X first;

    /**
     * second value
     */
    public Y second;

    /**
     * creates pair with 2 input values
     * @param first sets first value
     * @param second sets second value
     */
    public Pair(X first, Y second) {
        this.first = first;
        this.second = second;
    }

    /**
     * checks if Pair is equal to the given object
     * @param obj to check if equal to
     * @return if the objects are equal
     */
    @Override
    public boolean equals(Object obj){
        if (obj == null)
            return false;
        if (obj.getClass() != this.getClass())
            return false;

        if(this.first.getClass() != ((Pair<?, ?>) obj).first.getClass())
            return false;
        if(this.second.getClass() != ((Pair<?, ?>) obj).second.getClass())
            return false;

        final Pair<?, ?> other = (Pair<?, ?>) obj;
        return this.first == other.first && this.second == other.second;
    }
}

public class Pair<X, Y> {
    public X first;
    public Y second;

    public Pair(X first, Y second) {
        this.first = first;
        this.second = second;
    }

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

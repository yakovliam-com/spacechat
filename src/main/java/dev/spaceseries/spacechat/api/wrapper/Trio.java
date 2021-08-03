package dev.spaceseries.spacechat.api.wrapper;

public class Trio<L, M, R> {

    L left;

    R right;

    M mid;

    public Trio() {
    }

    public Trio(L l, M m, R r) {
        this.left = l;
        this.mid = m;
        this.right = r;
    }

    public L getLeft() {
        return left;
    }

    public void setLeft(L left) {
        this.left = left;
    }

    public R getRight() {
        return right;
    }

    public void setRight(R right) {
        this.right = right;
    }

    public M getMid() {
        return mid;
    }

    public void setMid(M mid) {
        this.mid = mid;
    }
}

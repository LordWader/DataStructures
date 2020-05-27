import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;
 
public class Main {
 
    public static void main(String[] args) {
        InputStream inputStream = System.in;
        OutputStream outputStream = System.out;
        InputReader in = new InputReader(inputStream);
        PrintWriter out = new PrintWriter(outputStream);
        Task solver = new Task();
        solver.solve(1, in, out);
        out.close();
    }
 
    static class MaxElement {
        long value;
        long index;
 
        MaxElement(long value, long index) {
            this.value = value;
            this.index = index;
        }
 
        @Override
        public String toString() {
            return this.value + " " + this.index;
        }
    }
 
    static class NaiveSegmentedTree {
        MaxElement[] tree;
        int size;
 
        NaiveSegmentedTree(int size) {
            this.init(size);
        }
 
        private void init(long n) {
            int size = 1;
            while (size < n) {
                size *= 2;
            }
            this.tree = new MaxElement[size * 2 - 1];
            for (int i = 0; i < size * 2 - 1; i++) {
                this.tree[i] = new MaxElement(0, i);
            }
            this.size = size;
        }
 
        private void setValue(int x) {
            if (this.tree[2 * x + 1].value >= this.tree[2 * x + 2].value) {
                this.tree[x].value = this.tree[2 * x + 1].value;
                this.tree[x].index = this.tree[2 * x + 1].index;
            } else {
                this.tree[x].value = this.tree[2 * x + 2].value;
                this.tree[x].index = this.tree[2 * x + 2].index;
            }
        }
 
        private void build(int[] a, int x, int lx, int rx) {
            if (rx - lx == 1) {
                if (lx < a.length) {
                    this.tree[x].value = a[lx];
                    this.tree[x].index = lx;
                }
            } else {
                int mid = (lx + rx) / 2;
                build(a, 2 * x + 1, lx, mid);
                build(a, 2 * x + 2, mid, rx);
                setValue(x);
            }
        }
 
        public void build(int[] a) {
            build(a, 0, 0, this.size);
        }
 
        private void set(long index, long value, int x, long lx, long rx) {
            if (rx - lx == 1) {
                this.tree[x].value = value;
                return;
            }
            long mid = (lx + rx) / 2;
            if (index < mid) {
                set(index, value, 2 * x + 1, lx, mid);
            } else {
                set(index, value, 2 * x + 2, mid, rx);
            }
            setValue(x);
        }
 
        public void set(long index, long value) {
            set(index, value, 0, 0, this.size);
        }
 
        private long firstAbove(long value, long left, int x, int lx, int rx) {
            if (rx <= left) {
                return -1;
            }
            if (tree[x].value < value) {
                return -1;
            }
            if (rx - lx == 1) {
                return tree[x].index;
            }
            int mid = (lx + rx) / 2;
            long res = firstAbove(value, left, 2 * x + 1, lx, mid);
            if (res == -1) {
                res = firstAbove(value, left, 2 * x + 2, mid, rx);
            }
            return res;
        }
 
        public long firstAbove(long k, int left) {
            return firstAbove(k, left, 0, 0, this.size);
        }
 
    }
 
    static class Task {
 
 
        public void solve(int testNumber, InputReader in, PrintWriter out) {
            int n, m;
            n = in.nextInt();
            m = in.nextInt();
            int[] a = new int[n];
            NaiveSegmentedTree tree = new NaiveSegmentedTree(n);
            for (int i = 0; i < n; i++) {
                a[i] = in.nextInt();
            }
            tree.build(a);
            for (int q = 0; q < m; q++) {
                int com;
                com = in.nextInt();
                if (com == 1) {
                    int i;
                    long val;
                    i = in.nextInt();
                    val = in.nextInt();
                    tree.set(i, val);
                } else {
                    int x;
                    int left;
                    x = in.nextInt();
                    left = in.nextInt();
                    out.println(tree.firstAbove(x, left));
                }
            }
        }
    }
 
    static class InputReader {
 
        public BufferedReader reader;
        public StringTokenizer tokenizer;
 
        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }
 
        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }
 
        public int nextInt() {
            return Integer.parseInt(next());
        }
 
        public float nextFloat() {
            return Float.parseFloat(next());
        }
    }
}
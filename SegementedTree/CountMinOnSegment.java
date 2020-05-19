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
 
    static class MinPair {
        long min;
        long count;
 
        MinPair(long min, long count) {
            this.min = min;
            this.count = count;
        }
 
        @Override
        public String toString() {
            return this.min + " " + this.count;
        }
    }
 
    static class NaiveSegmentedTree {
        MinPair[] tree;
        int size;
 
        NaiveSegmentedTree(int size) {
            this.init(size);
        }
 
        private void init(long n) {
            int size = 1;
            while (size < n) {
                size *= 2;
            }
            this.tree = new MinPair[size * 2 - 1];
            for (int i = 0; i < size * 2 - 1; i++) {
                this.tree[i] = new MinPair((long) 1e10, 0);
            }
            this.size = size;
        }
 
        private void build(int[] a, int x, int lx, int rx) {
            if (rx - lx == 1) {
                if (lx < a.length) {
                    this.tree[x].min = a[lx];
                    this.tree[x].count = 1;
                }
            } else {
                int mid = (lx + rx) / 2;
                build(a, 2 * x + 1, lx, mid);
                build(a, 2 * x + 2, mid, rx);
                setValueToTree(x);
            }
        }
 
        public void build(int[] a) {
            init(a.length);
            build(a, 0, 0, this.size);
        }
 
        private void set(long index, long value, int x, long lx, long rx) {
            if (rx - lx == 1) {
                this.tree[x].min = value;
                this.tree[x].count = 1;
                return;
            }
            long mid = (lx + rx) / 2;
            if (index < mid) {
                set(index, value, 2 * x + 1, lx, mid);
            } else {
                set(index, value, 2 * x + 2, mid, rx);
            }
            setValueToTree(x);
        }
 
        private void setValueToTree(int x) {
            if (this.tree[2 * x + 1].min < this.tree[2 * x + 2].min) {
                this.tree[x].min = this.tree[2 * x + 1].min;
                this.tree[x].count = this.tree[2 * x + 1].count;
            } else if (this.tree[2 * x + 1].min > this.tree[2 * x + 2].min) {
                this.tree[x].min = this.tree[2 * x + 2].min;
                this.tree[x].count = this.tree[2 * x + 2].count;
            } else {
                this.tree[x].min = this.tree[2 * x + 2].min;
                this.tree[x].count = this.tree[2 * x + 2].count + this.tree[2 * x + 1].count;
            }
        }
 
        public void set(long index, long value) {
            set(index, value, 0, 0, this.size);
        }
 
        private MinPair min(long l, long r, int x, long lx, long rx) {
            if (l >= rx || lx >= r) {
                return new MinPair((long) 1e10, 0);
            }
            if (lx >= l && rx <= r) {
                return this.tree[x];
            }
            long mid = (lx + rx) / 2;
            MinPair s1 = min(l, r, 2 * x + 1, lx, mid);
            MinPair s2 = min(l, r, 2 * x + 2, mid, rx);
            if (s1.min < s2.min) {
                return s1;
            } else if (s1.min > s2.min) {
                return s2;
            } else {
                return new MinPair(s1.min, s1.count + s2.count);
            }
        }
 
        public MinPair min(long l, long r) {
            return min(l, r, 0, 0, this.size);
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
                    int i, val;
                    i = in.nextInt();
                    val = in.nextInt();
                    tree.set(i, val);
                } else {
                    int l, r;
                    l = in.nextInt();
                    r = in.nextInt();
                    out.println(tree.min(l, r));
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

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
 
    static class NaiveSegmentedTree {
        long[] tree;
        int size;
 
        NaiveSegmentedTree(int size) {
            this.init(size);
        }
 
        private void init(long n) {
            int size = 1;
            while (size < n) {
                size *= 2;
            }
            this.tree = new long[size * 2 - 1];
            Arrays.fill(this.tree, 0);
            this.size = size;
        }
 
        private void build(int[] a, int x, int lx, int rx) {
            if (rx - lx == 1) {
                if (lx < a.length) {
                    this.tree[x] = a[lx];
                }
            } else {
                int mid = (lx + rx) / 2;
                build(a, 2 * x + 1, lx, mid);
                build(a, 2 * x + 2, mid, rx);
                this.tree[x] = this.tree[2 * x + 1] + this.tree[2 * x + 2];
            }
        }
 
        public void build(int[] a) {
            init(a.length);
            build(a, 0, 0, this.size);
        }
 
        private void set(long index, long value, int x, long lx, long rx) {
            if (rx - lx == 1) {
                this.tree[x] = value;
                return;
            }
            long mid = (lx + rx) / 2;
            if (index < mid) {
                set(index, value, 2 * x + 1, lx, mid);
            } else {
                set(index, value, 2 * x + 2, mid, rx);
            }
            this.tree[x] = this.tree[2 * x + 1] + this.tree[2 * x + 2];
 
        }
 
        public void set(long index, long value) {
            set(index, value, 0, 0, this.size);
        }
 
        private long sum(long l, long r, int x, long lx, long rx) {
            if (l >= rx || lx >= r) {
                return 0;
            }
            if (lx >= l && rx <= r) {
                return this.tree[x];
            }
            long mid = (lx + rx) / 2;
            long s1 = sum(l, r, 2 * x + 1, lx, mid);
            long s2 = sum(l, r, 2 * x + 2, mid, rx);
            return s1 + s2;
        }
 
        public long sum(long l, long r) {
            return sum(l, r, 0, 0, this.size);
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
                a[i] =  in.nextInt();
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
                    out.println(tree.sum(l, r));
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
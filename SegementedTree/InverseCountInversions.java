import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;
 
import static java.lang.Math.max;
 
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
                    this.tree[x] = 1;
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
 
        private void set(long index, int x, long lx, long rx) {
            if (rx - lx == 1) {
                this.tree[x] = 0;
                return;
            }
            long mid = (lx + rx) / 2;
            if (index < mid) {
                set(index, 2 * x + 1, lx, mid);
            } else {
                set(index, 2 * x + 2, mid, rx);
            }
            this.tree[x] = this.tree[2 * x + 1] + this.tree[2 * x + 2];
 
        }
 
        public void set(long index) {
            set(index, 0, 0, this.size);
        }
 
        private long findIndex(long toFind, int x, long lx, long rx) {
            if (rx - lx == 1 && toFind == 1 && tree[x] == 1) {
                return rx;
            }
            long mid = (lx + rx) / 2;
            if (tree[2 * x + 2] >= toFind) {
                return findIndex(toFind, 2 * x + 2, mid, rx);
            } else {
                return findIndex(toFind - tree[2 * x + 2], 2 * x + 1, lx, mid);
            }
        }
 
        public long findIndex(long toFind) {
            return findIndex(toFind, 0, 0, this.size);
        }
 
    }
 
    static class Task {
 
 
        public void solve(int testNumber, InputReader in, PrintWriter out) {
            int n;
            n = in.nextInt();
            int[] a = new int[n];
            NaiveSegmentedTree tree = new NaiveSegmentedTree(n);
            for (int i = 0; i < n; i++) {
                a[i] = in.nextInt();
            }
            tree.build(a);
            long[] ans = new long[n];
            for (int i = n - 1; i >= 0; i--) {
                long index = tree.findIndex(a[i] + 1);
                ans[i] = index;
                tree.set(index - 1);
            }
            for (int i =0; i< n; i++) {
                if (i == 0) {
                    out.print(ans[i]);
                } else {
                    out.print(" " + ans[i]);
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
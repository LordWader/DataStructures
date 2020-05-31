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
 
    static class Node {
        long seg, pref, suf, sum;
 
        Node(long seg, long pref, long suf, long sum) {
            this.seg = seg;
            this.pref = pref;
            this.suf = suf;
            this.sum = sum;
        }
 
//        @Override
//        public String toString() {
//            return this.min + " " + this.count;
//        }
    }
 
    static class NaiveSegmentedTree {
        Node[] tree;
        int size;
 
        NaiveSegmentedTree(int size) {
            this.init(size);
        }
 
        private void init(long n) {
            int size = 1;
            while (size < n) {
                size *= 2;
            }
            this.tree = new Node[size * 2 - 1];
            for (int i = 0; i < size * 2 - 1; i++) {
                this.tree[i] = new Node(0, 0, 0, 0);
            }
            this.size = size;
        }
 
        private Node combine(Node a, Node b) {
            return new Node(
                    max(a.seg, Math.max(b.seg, a.suf + b.pref)),
                    max(a.pref, a.sum + b.pref),
                    max(b.suf, b.sum + a.suf),
                    a.sum + b.sum
            );
        }
 
        private Node setOneElement(long x) {
            return new Node(
                    max(0, x),
                    max(0, x),
                    max(0, x),
                    x
            );
        }
 
        private void build(int[] a, int x, int lx, int rx) {
            if (rx - lx == 1) {
                if (lx < a.length) {
                    this.tree[x] = setOneElement(a[lx]);
                }
            } else {
                int mid = (lx + rx) / 2;
                build(a, 2 * x + 1, lx, mid);
                build(a, 2 * x + 2, mid, rx);
                tree[x] = combine(tree[2 * x + 1], tree[2 * x + 2]);
            }
        }
 
        public void build(int[] a) {
            init(a.length);
            build(a, 0, 0, this.size);
        }
 
        private void set(long index, long value, int x, long lx, long rx) {
            if (rx - lx == 1) {
                this.tree[x] = setOneElement(value);
                return;
            }
            long mid = (lx + rx) / 2;
            if (index < mid) {
                set(index, value, 2 * x + 1, lx, mid);
            } else {
                set(index, value, 2 * x + 2, mid, rx);
            }
            tree[x] = combine(tree[2 * x + 1], tree[2 * x + 2]);
        }
 
 
        public void set(long index, long value) {
            set(index, value, 0, 0, this.size);
        }
 
        private Node calc(long l, long r, int x, long lx, long rx) {
            if (l >= rx || lx >= r) {
                return new Node(0, 0, 0, 0);
            }
            if (lx >= l && rx <= r) {
                return this.tree[x];
            }
            long mid = (lx + rx) / 2;
            Node s1 = calc(l, r, 2 * x + 1, lx, mid);
            Node s2 = calc(l, r, 2 * x + 2, mid, rx);
            return combine(s1, s2);
        }
 
        public Node calc(long l, long r) {
            return calc(l, r, 0, 0, this.size);
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
            out.println(tree.tree[0].seg);
            for (int q = 0; q < m; q++) {
                int i, v;
                i = in.nextInt();
                v = in.nextInt();
                tree.set(i, v);
                out.println(tree.tree[0].seg);
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
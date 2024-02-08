import java.util.*;

class Node {
    char value;
    Node leftc;
    Node rightc;
    int posNumber;
    Set<Integer> firstpos;
    Set<Integer> lastpos;
    Set<Integer> followpos;
    boolean nullable;

    Node(char value) {
        this.value = value;
        firstpos = new HashSet<Integer>();
        lastpos = new HashSet<Integer>();
        followpos = new HashSet<Integer>();
        posNumber = 0;
    }
}

class State {
    ArrayList<Integer> value;
    boolean marked;

    State() {
        value = new ArrayList<Integer>();
    }

}

class Transition {
    State from;
    State to;
    char value;

    Transition(State from, State to, char value) {
        this.from = from;
        this.to = to;
        this.value = value;
    }

}

class Tree {
    Node root;
    int count = 0;
    Set<Character> alphabet;
    ArrayList<Node> leaves;
    ArrayList<State> Dstates;
    ArrayList<Transition> Dtrans;

    Tree() {
        root = null;
        leaves = new ArrayList<Node>();
        alphabet = new HashSet<Character>();
        Dstates = new ArrayList<State>();
        Dtrans = new ArrayList<Transition>();
    }

    void parseRegex(String regex) {
        Stack<Character> st = new Stack<>();
        for (int i = 0; i < regex.length(); i++) {
            if (regex.charAt(i) == '(') {
                int j = i + 1;
                while (regex.charAt(j) != ')') {
                    st.push(regex.charAt(j));
                    if (Character.isLetter(regex.charAt(j))) {
                        count++;
                        alphabet.add(regex.charAt(j));
                    }
                    j++;
                }
                j++;
                char c1 = st.pop();
                char c2 = st.pop();
                char c3 = st.pop();
                Node n1 = new Node(c1);
                Node n2 = new Node(c2);
                Node n3 = new Node(c3);
                n2.leftc = n3;
                n2.rightc = n1;
                i = j;
                root = n2;

            }
            if (regex.charAt(i) == '*') {
                Node temp = new Node('*');
                temp.leftc = root;
                root = temp;
            }
            if (Character.isLetter(regex.charAt(i))) {
                count++;
                alphabet.add(regex.charAt(i));
                if (root != null) {
                    if (root.value != '.') {
                        Node temp = new Node('.');
                        temp.leftc = root;
                        temp.rightc = new Node(regex.charAt(i));
                        root = temp;
                    } else {
                        if (root.rightc != null) {
                            Node temp = new Node('.');
                            temp.leftc = root;
                            temp.rightc = new Node(regex.charAt(i));
                            root = temp;
                        } else {
                            root.rightc = new Node(regex.charAt(i));
                        }
                    }
                } else {
                    Node temp = new Node('.');
                    temp.leftc = new Node(regex.charAt(i));
                }
            }
        }
        Node temp = new Node('.');
        temp.rightc = new Node('#');
        temp.leftc = root;
        root = temp;
        count++;
    }

    void printTree() {
        // format the output
        System.out.println("Value | Left Child | Right Child | Nullable | Firstpos | Lastpos | Followpos");
        printTree(root);
    }

    void printTree(Node n) {
        if (n == null) {
            return;
        }
        System.out.print(n.value + " | ");
        if (n.leftc != null) {
            System.out.print(n.leftc.value + " | ");
        } else {
            System.out.print("null | ");
        }
        if (n.rightc != null) {
            System.out.print(n.rightc.value + " | ");
        } else {
            System.out.print("null | ");
        }
        System.out.print(n.nullable + " | ");
        System.out.print(n.firstpos + " | ");
        System.out.print(n.lastpos + " | ");
        System.out.print(n.followpos + " | ");
        System.out.println();
        printTree(n.leftc);
        printTree(n.rightc);
    }

    void numberLeaves(Node n) {
        if (isLeaf(n)) {
            n.posNumber = count;
            n.firstpos.add(count);
            n.lastpos.add(count);
            leaves.add(0, n);
            count--;
            return;
        } else if (n.value == '*') {
            numberLeaves(n.leftc);
        } else {
            numberLeaves(n.rightc);
            numberLeaves(n.leftc);
        }
    }

    void assignNullable(Node n) {
        if (n.value == '|') {
            n.nullable = n.leftc.nullable || n.rightc.nullable;
            assignNullable(n.leftc);
            assignNullable(n.rightc);
        } else if (n.value == '.') {
            n.nullable = n.leftc.nullable && n.rightc.nullable;
            assignNullable(n.leftc);
            assignNullable(n.rightc);
        } else if (n.value == '*') {
            n.nullable = true;
            assignNullable(n.leftc);
        } else {
            n.nullable = false;
        }
    }

    void assignFirstLastPos(Node n) {
        if (n.value == '|') {
            assignFirstLastPos(n.leftc);
            assignFirstLastPos(n.rightc);

            Set<Integer> temp1 = new HashSet<Integer>();
            temp1.addAll(n.leftc.firstpos);
            temp1.addAll(n.rightc.firstpos);
            n.firstpos.addAll(temp1);

            Set<Integer> temp2 = new HashSet<Integer>();
            temp2.addAll(n.leftc.lastpos);
            temp2.addAll(n.rightc.lastpos);
            n.lastpos.addAll(temp2);

        } else if (n.value == '.') {
            assignFirstLastPos(n.leftc);
            assignFirstLastPos(n.rightc);
            if (n.leftc.nullable) {
                Set<Integer> temp1 = new HashSet<Integer>();
                temp1.addAll(n.leftc.firstpos);
                temp1.addAll(n.rightc.firstpos);
                n.firstpos.addAll(temp1);
            } else {
                n.firstpos.addAll(n.leftc.firstpos);
            }

            if (n.rightc.nullable) {
                Set<Integer> temp1 = new HashSet<Integer>();
                temp1.addAll(n.leftc.lastpos);
                temp1.addAll(n.rightc.lastpos);
                n.lastpos.addAll(temp1);
            } else {
                n.lastpos.addAll(n.rightc.lastpos);
            }
        } else if (n.value == '*') {
            assignFirstLastPos(n.leftc);
            n.firstpos.addAll(n.leftc.firstpos);
            n.lastpos.addAll(n.leftc.lastpos);
        } else {
            return;
        }
    }

    void calculateFollowPos(Node n) {
        if (n.value == '.') {
            Iterator<Integer> it = n.leftc.lastpos.iterator();
            while (it.hasNext()) {
                int i = it.next();
                Set<Integer> temp = new HashSet<Integer>();
                temp.addAll(n.rightc.firstpos);
                temp.addAll(leaves.get(i - 1).followpos);
                leaves.get(i - 1).followpos.addAll(temp);
            }
        } else if (n.value == '*') {
            Iterator<Integer> it = n.lastpos.iterator();
            while (it.hasNext()) {
                int i = it.next();
                Set<Integer> temp = new HashSet<Integer>();
                temp.addAll(n.firstpos);
                temp.addAll(leaves.get(i - 1).followpos);
                leaves.get(i - 1).followpos.addAll(temp);
            }
        }
    }

    void assignFollowPos(Node n) {
        if (n == null) {
            return;
        } else {
            calculateFollowPos(n);
            assignFollowPos(n.leftc);
            assignFollowPos(n.rightc);
        }
    }

void constructDstates() { State s0 = new State();
s0.value.addAll(root.firstpos); Dstates.add(s0);

Queue<State> queue = new LinkedList<>(); queue.add(s0);

// Set to keep track of processed states 
Set<Set<Integer>> processedStates = new HashSet<>(); 

processedStates.add(new HashSet<>(s0.value)); // Convert ArrayList<Integer> to Set<Integer>

while (!queue.isEmpty()) {
State currentState = queue.poll();

for (char a : alphabet) { Set<Integer> U = new HashSet<>();
for (int p : currentState.value) { Node node = leaves.get(p - 1); if (node.value == a) {
U.addAll(node.followpos);
}
}

if (!processedStates.contains(U)) { 
    State newState = new State(); 
    newState.value.addAll(U); 
    Dstates.add(newState); 
    queue.add(newState); 
    processedStates.add(U);
}

State newState = getStateByValue(Dstates, U);
Dtrans.add(new Transition(currentState, newState, a));
}
}
}

void printDFA(){
System.out.println('\n' + "DFA States: "); for (Transition t : Dtrans) {
System.out.println(t.from.value + " -> " + t.to.value + ": " + t.value);
}
}

    boolean containsState(ArrayList<State> states, Set<Integer> value) {
        for (State state : states) {
            if (state.value.equals(value)) {
                return true;
            }
        }
        return false;
    }

State getStateByValue(ArrayList<State> states, Set<Integer> value)
{
for (State state : states) {
if (state.value.size() != value.size()) {
continue; // If sizes are different, sets cannot be equal
}

boolean equalSets = true;
for (int pos : state.value) { if (!value.contains(pos)) {
equalSets = false; break;
}
}

if (equalSets) { return state;
}
}
return null;
}

    boolean isLeaf(Node n) {
        return n.leftc == null && n.rightc == null;
    }

    State getUnmarkedState() {
        for (int i = 0; i < Dstates.size(); i++) {
            if (!Dstates.get(i).marked) {
                return Dstates.get(i);
            }
        }
        return null;
    }

    boolean checkAllMarked() {
        for (int i = 0; i < Dstates.size(); i++) {
            if (!Dstates.get(i).marked) {
                return false;
            }
        }
        return true;
    }
}

class parseTree {
    public static void main(String args[]) {
        Tree t = new Tree();
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the regular expression: ");
        String regex = sc.nextLine();
        t.parseRegex(regex);
        t.numberLeaves(t.root);
        t.assignNullable(t.root);
        t.assignFirstLastPos(t.root);
        t.assignFollowPos(t.root);
        t.constructDstates();
        t.printTree();
        t.printDFA();
        sc.close();
    }
}

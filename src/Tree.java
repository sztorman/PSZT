//Implementacja Drzewa
//(c) Dominik Goździuk
//www.algorytm.org

import java.util.LinkedList;

interface INode < T >  {
    public Node < T > getParent();// zwraca referencje rodzica
    public void setParent(Node < T > parent);// ustawia rodzica dla węzła
    public T getData();// zwraca przechowywane dane
    public void setData(T data);// ustawia dane w węźle
    public int getDegree();// zwraca stopień węzła
    public Node < T > getChild(int i);// zwraca referencje do i-tego dziecka
    public boolean isLeaf();// sprawdza czy węzeł jest liściem
    public Node < T > addChild(Node < T > child);// dodaje do węzła dziecko (inny węzeł)
    public Node < T > addChild(T data);// tworzy i dodaje do węzła dziecko z danymi
    public Node < T > removeChild(int i);// usuwa i-te dziecko węzła
    public void removeChildren();// usuwa wszystkie dzieci węzła
    public Node < T > getLeftMostChild();// zwraca pierwsze dziecko węzła (z lewej)
    public LinkedList < Node < T >> getChildren();// zwraca listę dzieci
    public Node < T > getRightSibling();// zwraca kolejny element siostrzany węzła
    public String toString();// wyświetla węzeł (najczęściej dane)
}

class Node < T > implements INode < T >  {
    private T data;// dane
    private Node < T > parent;// referencja do rodzica
    private LinkedList < Node < T >> children;// lista dzieci

    public Node()  {// konstruktor domyślny
        parent = null;
        children = new LinkedList < Node < T >> ();
    }

    public Node(Node < T > parent)  {// konstruktor jednoparametrowy
        this();
        this.parent = parent;
    }

    public Node(Node < T > parent, T data)  {// konstruktor dwuparametrowy
        this(parent);
        this.data = data;
    }


    public Node < T > getParent()  {
        return parent;
    }

    public void setParent(Node < T > parent)  {
        this.parent = parent;
    }

    public T getData()  {
        return data;
    }

    public void setData(T data)  {
        this.data = data;
    }

    public int getDegree()  {
        return children.size();
    }

    public Node < T > getChild(int i)  {
        return children.get(i);
    }

    public boolean isLeaf()  {
        return children.isEmpty();
    }

    public Node < T > addChild(Node < T > child)  {
        child.setParent(this);
        children.add(child);
        return child;
    }

    public Node < T > addChild(T data)  {
        Node < T > child = new Node < T > (this, data);
        children.add(child);
        return child;
    }

    public Node < T > removeChild(int i)  {
        return children.remove(i);
    }

    public void removeChildren()  {
        children.clear();
    }

    public Node < T > getLeftMostChild()  {
        if (children.isEmpty())
            return null;
        return children.get(0);
    }

    public LinkedList < Node < T >> getChildren()  {
        if (children.isEmpty())
            return null;
        return children;
    }

    public Node < T > getRightSibling()  {
        if (parent != null) {
            LinkedList < Node < T >> childrenParent = parent.getChildren();
            int pozycja = childrenParent.indexOf(this);
            if (childrenParent.size() > pozycja + 1)
                return childrenParent.get(pozycja + 1) ;
        }
        return null;
    }

    public String toString()  {
        return data.toString();
    }
}

class Tree < T >  {
    private Node < T > root;// referencja do korzenia

    public Tree()  {// konstruktor domyślny
        root = null;
    }

    public Tree(Node < T > root)  {// konstruktor jednoparametrowy
        this.root = root;
    }

    public Node < T > getRoot()  {
        return root;
    }

    public void preOrder(Node < T > n)  {
        System.out.print(n + " ");
        Node < T > temp = n.getLeftMostChild();
        while (temp != null) {
            preOrder(temp);
            temp = temp.getRightSibling();
        }
    }

    public void inOrder(Node < T > n)  {
        if (n.isLeaf())
            System.out.print(n + " ") ;
        else  {
            Node < T > temp = n.getLeftMostChild();
            inOrder(temp);
            System.out.print(n + " ");
            temp = temp.getRightSibling();
            while (temp != null) {
                inOrder(temp);
                temp = temp.getRightSibling();
            }
        }
    }

    public void postOrder(Node < T > n)  {
        Node < T > temp = n.getLeftMostChild();
        while (temp != null) {
            postOrder(temp);
            temp = temp.getRightSibling();
        }
        System.out.print(n + " ");
    }


}
public class CircularLinkedList {
    
    private Node head = null;
    private Node tail = null;
    private int lenght = 0;

    public Node getHead(){
        return this.head;
    }

    public Node getTail(){
        return this.tail;
    }

    public int lenght(){
        return this.lenght;
    }

    public void addNode(String AID, String Address) {
        Node newNode = new Node(AID, Address);
     
        if (head == null) head = newNode;
        else tail.nextNode = newNode;
     
        tail = newNode;
        tail.nextNode = head;
        this.lenght += 1;
    }

    public boolean containsNode(String searchValue) {
        Node currentNode = head;
     
        if (head == null) return false;
        else {
            do {
                if (currentNode.AID == searchValue) {
                    return true;
                }
                currentNode = currentNode.nextNode;
            } while (currentNode != head);
            return false;
        }
    }
    
}
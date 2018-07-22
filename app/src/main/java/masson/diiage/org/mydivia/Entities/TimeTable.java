package masson.diiage.org.mydivia.Entities;

public class TimeTable {
    String arret;
    String direction;
    String time;
    String Next;

    public TimeTable() {
    }

    public TimeTable(String arret, String direction, String time, String next) {
        this.arret = arret;
        this.direction = direction;
        this.time = time;
        Next = next;
    }

    public String getArret() {
        return arret;
    }

    public void setArret(String arret) {
        this.arret = arret.trim();
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction.trim();
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNext() {
        return Next;
    }

    public void setNext(String next) {
        Next = next;
    }
}

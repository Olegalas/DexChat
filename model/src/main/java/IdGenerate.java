import javax.persistence.*;

/**
 * Created by dexter on 29.03.16.
 */

@MappedSuperclass
public class IdGenerate {

    @Id // primary key
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    public IdGenerate() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IdGenerate that = (IdGenerate) o;

        return id == that.id;

    }

    @Override
    public int hashCode() {
        return id;
    }

}

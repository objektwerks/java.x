package persistence.jpa;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import static org.junit.Assert.assertNotSame;
import org.junit.Test;

public class EntityDiffAnalyzerTest {
    private static final EntityDiffAnalyzer analyzer = EntityDiffAnalyzer.newInstance(Diff.class);

    @Test
    public void diff() {
        Diff diffOne = create("one", "two");
        Diff diffTwo = create("three", "four");
        System.out.println("*** Diff by Field ***" + analyzer.diffByField(diffOne, diffTwo));
        System.out.println("*** Diff by Method ***" + analyzer.diffByMethod(diffOne, diffTwo, "getFieldTwo"));
        assertNotSame(diffOne, diffTwo);
    }

    private Diff create(String fieldOne, String fieldTwo) {
        Diff diff = new Diff(fieldOne, fieldTwo);
        Set<String> diffs = new HashSet<String>();
        diffs.add(diff.getFieldTwo());
        diff.setDiffs(diffs);
        return diff;
    }

    private class Diff implements Comparable<Diff>, Serializable {
        private String fieldOne;
        private transient String fieldTwo;
        private Set<String> diffs;

        private Diff() {
        }

        private Diff(String fieldOne, String fieldTwo) {
            this();
            this.fieldOne = fieldOne;
            this.fieldTwo = fieldTwo;
        }

        public String getFieldOne() {
            return fieldOne;
        }

        public String getFieldTwo() {
            return fieldTwo;
        }

        public Set<String> getDiffs() {
            return diffs;
        }

        public void setDiffs(Set<String> diffs) {
            this.diffs = diffs;
        }

        public int compareTo(Diff otherDiff) {
            return new CompareToBuilder()
                    .append(fieldOne, otherDiff.getFieldOne())
                    .append(fieldTwo, otherDiff.getFieldTwo())
                    .append(diffs, otherDiff.getDiffs())
                    .toComparison();
        }

        @Override
        public boolean equals(Object object) {
            if (object == null) return false;
            if (object == this) return true;
            Diff otherDiff = (Diff) object;
            boolean isEqual = new EqualsBuilder()
                    .append(fieldOne, otherDiff.getFieldOne())
                    .append(fieldTwo, otherDiff.getFieldTwo())
                    .append(diffs, otherDiff.getDiffs())
                    .isEquals();
            return isEqual;
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder()
                    .append(fieldOne)
                    .append(fieldTwo)
                    .append(diffs)
                    .toHashCode();
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .append(fieldOne)
                    .append(fieldTwo)
                    .append(diffs)
                    .toString();
        }
    }
}
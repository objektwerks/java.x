package xml.processing;

public interface XsdValidator {
    public void validate(String xml);

    public boolean isEnabled();
}
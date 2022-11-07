package by.modsen.meetup.filter;

public enum SortField {
    TOPIC("topic"),
    ORGANIZATION("organization"),
    PLACE("place"),
    DT_MEETUP("dtMeetup");

    private final String fieldName;

    SortField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}

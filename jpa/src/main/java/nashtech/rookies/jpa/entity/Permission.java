package nashtech.rookies.jpa.entity;

public enum Permission {

    USER_CREATED(Permission.bytesOfInt(2001)),
    USER_LISTED(Permission.bytesOfInt(1002)),
    USER_VIEW(Permission.bytesOfInt(1003)),
    USER_EDIT(Permission.bytesOfInt(1004)),
    DEPARTMENT_CREATED(Permission.bytesOfInt(2001)),
    DEPARTMENT_LISTED(Permission.bytesOfInt(2002)),
    DEPARTMENT_VIEW(Permission.bytesOfInt(2003)),
    DEPARTMENT_EDIT(Permission.bytesOfInt(2004));

    private final byte[] value;


    Permission (byte[] value) {
        this.value = value;
    }

    public byte[] getValue () {
        return this.value;
    }

    private static byte[] bytesOfInt (int data) {
        byte[] result = new byte[4];
        result[0] = (byte) ((data & 0xFF000000) >> 24);
        result[1] = (byte) ((data & 0x00FF0000) >> 16);
        result[2] = (byte) ((data & 0x0000FF00) >> 8);
        result[3] = (byte) ((data & 0x000000FF));
        return result;
    }
}

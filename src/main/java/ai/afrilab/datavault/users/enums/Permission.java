package ai.afrilab.datavault.users.enums;

public enum Permission {
    SUPER_ADMIN_READ("super_admin:read"),
    SUPER_ADMIN_UPDATE("super_admin:update"),
    SUPER_ADMIN_CREATE("super_admin:create"),
    SUPER_ADMIN_DELETE("super_admin:delete"),

    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),

    UPLOADER_READ("uploader:read"),
    UPLOADER_UPDATE("uploader:update"),
    UPLOADER_CREATE("uploader:create"),
    UPLOADER_DELETE("uploader:delete"),

    WORKER_READ("worker:read"),
    WORKER_UPDATE("worker:update"),
    WORKER_CREATE("worker:create"),
    WORKER_DELETE("worker:delete"),
    ;

    private final String permission;

    private Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return this.permission;
    }
}

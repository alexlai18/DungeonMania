package dungeonmania.response.models;

public final class ItemResponse {
    private final String id;
    private final String type;

    public ItemResponse(String id, String type) {
        this.id = id;
        this.type = type;
    }

    public final String getType() {
        return type;
    }

    public final String getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) {
            return false;
        } 

        if (getClass() != obj.getClass()) return false;
        
        ItemResponse other = (ItemResponse) obj;
        return other.getId().equals(this.id) && other.getType().equals(this.type);
    }
}

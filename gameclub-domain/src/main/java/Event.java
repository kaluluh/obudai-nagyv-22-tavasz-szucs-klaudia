import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Event {
    private Long id;
    private LocalDateTime date;
    private String place;
    private List<User> participants;
}

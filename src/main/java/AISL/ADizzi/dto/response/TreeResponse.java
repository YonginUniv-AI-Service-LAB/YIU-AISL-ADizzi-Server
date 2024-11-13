package AISL.ADizzi.dto.response;

import AISL.ADizzi.entity.Room;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "구조 정보")
public class TreeResponse {


    @Schema(description = "방 아이디")
    private Long roomId;
    @Schema(description = "방 이름")
    private String title;
    @Schema(description = "방 아이콘")
    private Long icon;
    @Schema(description = "방 색상")
    private Long color;
    @Schema(description = "수납장 목록")
    private List<ContainerInfo> containers;

    @Data
    @Schema(description = "수납장 구조 정보")
    public static class ContainerInfo {
        @Schema(description = "수납장 아이디")
        private Long containerId;
        @Schema(description = "수납장 이름")
        private String title;
        @Schema(description = "수납칸 목록")
        private List<SlotInfo> slots;
    }

    @Data
    @Schema(description = "수납칸 구조 정보")
    public static class SlotInfo {
        @Schema(description = "수납칸 아이디")
        private Long slotId;
        @Schema(description = "수납칸 이름")
        private String title;
    }

    public TreeResponse(Room room){
        this.roomId = room.getId();
        this.title = room.getTitle();
        this.icon = room.getIcon();
        this.color = room.getColor();
        // 수납장 목록을 설정하는 로직 추가 (예시)
        if (room.getContainers() != null) {
            List<ContainerInfo> containerInfoList = room.getContainers().stream()
                    .map(container -> {
                        ContainerInfo containerInfo = new ContainerInfo();
                        containerInfo.setContainerId(container.getId()); // 수납장 ID
                        containerInfo.setTitle(container.getTitle()); // 수납장 이름

                        // 수납칸 목록을 설정하는 로직 추가 (예시)
                        if (container.getSlots() != null) {
                            List<SlotInfo> slotInfoList = container.getSlots().stream()
                                    .map(slot -> {
                                        SlotInfo slotInfo = new SlotInfo();
                                        slotInfo.setSlotId(slot.getId()); // 수납칸 ID
                                        slotInfo.setTitle(slot.getTitle()); // 수납칸 이름
                                        return slotInfo;
                                    })
                                    .toList(); // Java 16 이상에서 사용 가능
                            containerInfo.setSlots(slotInfoList);
                        }

                        return containerInfo;
                    })
                    .toList(); // Java 16 이상에서 사용 가능
            this.containers = containerInfoList;
        }
    }
}

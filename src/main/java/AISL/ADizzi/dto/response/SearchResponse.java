package AISL.ADizzi.dto.response;

import AISL.ADizzi.entity.Room;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "검색 정보")
public class SearchResponse {

    private List<RoomResponse> rooms;

    private List<ContainerResponse> containers;

    private List<SlotResponse> slots;

    private List<ItemResponse> items;


}

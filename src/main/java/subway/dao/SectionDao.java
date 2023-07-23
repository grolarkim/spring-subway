package subway.dao;

import subway.domain.Section;

import java.util.List;
import java.util.Optional;

public interface SectionDao {
    Optional<Section> insert(Section section);

    List<Section> findAllByLineId(long lineId);

    boolean existByLineId(Long lineId);

    boolean existByLineIdAndStationId(Long lineId, Long stationId);

    void deleteById(Long id);

    Optional<Section> findByLineIdAndUpStationId(Long lineId, Long upStationId);

    Optional<Section> findByLineIdAndDownStationId(Long lineId, Long downStationId);

    void update(Section section);
}

package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import subway.dto.SectionRequest;

@DisplayName("지하철 구간 관련 기능 인수 테스트")
class SectionIntegrationTest extends IntegrationTest {

    private Long lineId;
    private SectionRequest sectionRequest1;
    private SectionRequest sectionRequest2;

    @BeforeEach
    public void setUp() {
        super.setUp();

        lineId = 1L;
        sectionRequest1 = new SectionRequest(1L, 2L, 10L);
        sectionRequest2 = new SectionRequest(2L, 3L, 10L);
    }

    @DisplayName("지하철 구간을 생성한다.")
    @Test
    void createSection() {
        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest1)
                .when().post("/lines/{lineId}/sections", lineId)
                .then().log().all().
                extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("노선이 존재하지 않는데 지하철 구간을 생성할 시 예외 발생")
    @Test
    void createSectionLineNotFound() {
        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest1)
                .when().post("/lines/{lineId}/sections", 99999)
                .then().log().all().
                extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("상행역이 존재하지 않는데 지하철 구간을 생성할 시 예외 발생")
    @Test
    void createSectionUpStationNotFound() {
        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new SectionRequest(99999L, 2L, 10L))
                .when().post("/lines/{lineId}/sections", 1L)
                .then().log().all().
                extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("하행역이 존재하지 않는데 지하철 구간을 생성할 시 예외 발생")
    @Test
    void createSectionDownStationNotFound() {
        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new SectionRequest(1L, 99999L, 10L))
                .when().post("/lines/{lineId}/sections", 1L)
                .then().log().all().
                extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("입력값 잘못된 상태로 지하철 구간 생성시 예외 발생")
    @Test
    void createSectionValidationFail() {
        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new SectionRequest(1L, -1L, 10L))
                .when().post("/lines/{lineId}/sections", 1L)
                .then().log().all().
                extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteSection() {
        // given
        ExtractableResponse<Response> createResponse1 = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest1)
                .when().post("/lines/{lineId}/sections", lineId)
                .then().log().all().
                extract();
        ExtractableResponse<Response> createResposne2 = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionRequest2)
                .when().post("/lines/{lineId}/sections", lineId)
                .then().log().all().
                extract();

        // when

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .queryParam("stationId", sectionRequest2.getDownStationId())
                .when().delete("/lines/{lineId}/sections", lineId)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }


}

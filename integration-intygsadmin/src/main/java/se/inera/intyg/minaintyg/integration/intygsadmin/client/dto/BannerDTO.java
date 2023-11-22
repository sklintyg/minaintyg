package se.inera.intyg.minaintyg.integration.intygsadmin.client.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.inera.intyg.minaintyg.integration.api.banner.model.Application;
import se.inera.intyg.minaintyg.integration.api.banner.model.BannerPriority;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BannerDTO implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;
  public static final String FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
  private UUID id;
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonFormat(pattern = FORMAT)
  private LocalDateTime createdAt;
  private Application application;
  private String message;
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonFormat(pattern = FORMAT)
  private LocalDateTime displayFrom;
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonFormat(pattern = FORMAT)
  private LocalDateTime displayTo;
  private BannerPriority priority;
}

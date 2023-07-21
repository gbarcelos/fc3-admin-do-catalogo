package com.fullcycle.admin.catalogo.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record VideoEncoderError(
    @JsonProperty("message") VideoMessage message, @JsonProperty("error") String error)
    implements VideoEncoderResult {

  private static final String ERROR = "ERROR";

  @Override
  public String getStatus() {
    return ERROR;
  }
}

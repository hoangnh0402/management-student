package com.example.demo.domain.dto;

import com.example.demo.config.AccessDeniedException;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

@Component("WebTransferRequestHeader")
public class RequestHeader {
  private final HttpServletRequest httpServletRequest;

  public RequestHeader(HttpServletRequest httpServletRequest) {
    this.httpServletRequest = httpServletRequest;
  }

  /**
   * @return String
   */
  public String getUsername() {
    String bearerToken = httpServletRequest.getHeader("Authorization");
    if (StringUtils.isEmpty(bearerToken)) {
      return "ANONYMOUS";
    }
    String token = bearerToken.substring(7);
    try {
      SignedJWT decodedJWT = SignedJWT.parse(token);
      return (String) decodedJWT.getPayload().toJSONObject().get("username");
    } catch (ParseException e) {
      throw new AccessDeniedException();
    }
  }

}

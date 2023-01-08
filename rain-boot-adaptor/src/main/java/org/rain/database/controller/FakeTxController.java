package org.rain.database.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * .
 *
 * @author ZFM.
 * @date 2023/1/7 21:47.
 */
@RestController
@RequestMapping("/fake-tx")
public class FakeTxController {

    public ResponseEntity<String> dbOperation(@PathVariable(value = "executeKey") String executeKey, @RequestBody String requestBody) {
        executeKey = new String(Base64.getUrlDecoder().decode(executeKey), StandardCharsets.UTF_8);
        String resultStr = FakeTxClientCentre.executeWithBoundSqlSession(executeKey, sqlSession -> DbOperationHandle.handleWithPrecessedException(requestBody, sqlSession));
        return ResponseEntity.ok(resultStr);
    }

}

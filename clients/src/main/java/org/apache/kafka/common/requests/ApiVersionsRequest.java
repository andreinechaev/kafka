/**
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements. See the NOTICE
 * file distributed with this work for additional information regarding copyright ownership. The ASF licenses this file
 * to You under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.apache.kafka.common.requests;

import org.apache.kafka.common.protocol.ApiKeys;
import org.apache.kafka.common.protocol.Errors;
import org.apache.kafka.common.protocol.ProtoUtils;
import org.apache.kafka.common.protocol.types.Schema;
import org.apache.kafka.common.protocol.types.Struct;

import java.nio.ByteBuffer;
import java.util.Collections;

public class ApiVersionsRequest extends AbstractRequest {
    private static final Schema CURRENT_SCHEMA = ProtoUtils.currentRequestSchema(ApiKeys.API_VERSIONS.id);
    public static final ApiVersionsRequest API_VERSIONS_REQUEST = new ApiVersionsRequest();

    public ApiVersionsRequest() {
        super(new Struct(CURRENT_SCHEMA));
    }

    public ApiVersionsRequest(Struct struct) {
        super(struct);
    }

    @Override
    public AbstractResponse getErrorResponse(int versionId, Throwable e) {
        switch (versionId) {
            case 0:
                short errorCode = Errors.forException(e).code();
                return new ApiVersionsResponse(errorCode, Collections.<ApiVersionsResponse.ApiVersion>emptyList());
            default:
                throw new IllegalArgumentException(String.format("Version %d is not valid. Valid versions for %s are 0 to %d",
                        versionId, this.getClass().getSimpleName(), ProtoUtils.latestVersion(ApiKeys.API_VERSIONS.id)));
        }
    }

    public static ApiVersionsRequest parse(ByteBuffer buffer, int versionId) {
        return new ApiVersionsRequest(ProtoUtils.parseRequest(ApiKeys.API_VERSIONS.id, versionId, buffer));
    }

    public static ApiVersionsRequest parse(ByteBuffer buffer) {
        return new ApiVersionsRequest(CURRENT_SCHEMA.read(buffer));
    }
}
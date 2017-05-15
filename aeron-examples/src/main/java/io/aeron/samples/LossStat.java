/*
 * Copyright 2017 Real Logic Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.aeron.samples;

import io.aeron.driver.reports.LossReportReader;
import io.aeron.driver.reports.LossReportUtil;
import org.agrona.IoUtil;
import org.agrona.concurrent.AtomicBuffer;
import org.agrona.concurrent.UnsafeBuffer;

import java.io.File;
import java.nio.MappedByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

import static io.aeron.CommonContext.AERON_DIR_PROP_DEFAULT;
import static io.aeron.CommonContext.AERON_DIR_PROP_NAME;
import static java.lang.System.getProperty;

/**
 * Application that prints a report of loss observed by stream to STDOUT.
 */
public class LossStat {
    public static void main(final String[] args) {
        final String aeronDirectoryName = getProperty(AERON_DIR_PROP_NAME, AERON_DIR_PROP_DEFAULT);
        final File lossReportFile = LossReportUtil.file(aeronDirectoryName);

        if (!lossReportFile.exists()) {
            System.err.print("Loss report does not exist: " + lossReportFile);
            System.exit(1);
        }

        final MappedByteBuffer mappedByteBuffer = IoUtil.mapExistingFile(lossReportFile, "Loss Report");
        final AtomicBuffer buffer = new UnsafeBuffer(mappedByteBuffer);
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");

        System.out.println(
                "#OBSERVATION_COUNT, TOTAL_BYTES_LOST, FIRST_OBSERVATION," +
                        " LAST_OBSERVATION, SESSION_ID, STREAM_ID, CHANNEL, SOURCE");

        final int entriesRead = LossReportReader.read(
                buffer,
                (
                        observationCount, totalBytesLost, firstObservationTimestamp, lastObservationTimestamp,
                        sessionId, streamId, channel, source
                ) ->
                        System.out.format(
                                "%d,%d,%s,%s,%d,%d,%s,%s%n",
                                observationCount,
                                totalBytesLost,
                                dateFormat.format(new Date(firstObservationTimestamp)),
                                dateFormat.format(new Date(lastObservationTimestamp)),
                                sessionId,
                                streamId,
                                channel,
                                source));

        System.out.println(entriesRead + " entries read");
    }
}

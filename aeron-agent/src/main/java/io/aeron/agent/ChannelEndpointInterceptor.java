/*
 * Copyright 2016 Real Logic Ltd.
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
package io.aeron.agent;

import io.aeron.driver.media.ReceiveChannelEndpoint;
import io.aeron.driver.media.SendChannelEndpoint;
import net.bytebuddy.asm.Advice;
import org.agrona.concurrent.UnsafeBuffer;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import static io.aeron.agent.EventCode.*;
import static io.aeron.agent.EventLogger.LOGGER;

public class ChannelEndpointInterceptor
{
    public static class SenderProxyInterceptor
    {
        public static class RegisterSendChannelEndpoint
        {
            @Advice.OnMethodEnter
            public static void registerSendChannelEndpoint(final SendChannelEndpoint channelEndpoint)
            {
                LOGGER.logChannelCreated(SEND_CHANNEL_CREATION, channelEndpoint.udpChannel().description());
            }
        }

        public static class CloseSendChannelEndpoint
        {
            @Advice.OnMethodEnter
            public static void closeSendChannelEndpoint(final SendChannelEndpoint channelEndpoint)
            {
                LOGGER.logChannelCreated(SEND_CHANNEL_CLOSE, channelEndpoint.udpChannel().description());
            }
        }
    }

    public static class ReceiverProxyInterceptor
    {
        public static class RegisterReceiveChannelEndpoint
        {
            @Advice.OnMethodEnter
            public static void registerReceiveChannelEndpoint(final ReceiveChannelEndpoint channelEndpoint)
            {
                LOGGER.logChannelCreated(RECEIVE_CHANNEL_CREATION, channelEndpoint.udpChannel().description());
            }
        }

        public static class CloseReceiveChannelEndpoint
        {
            @Advice.OnMethodEnter
            public static void closeReceiveChannelEndpoint(final ReceiveChannelEndpoint channelEndpoint)
            {
                LOGGER.logChannelCreated(RECEIVE_CHANNEL_CLOSE, channelEndpoint.udpChannel().description());
            }
        }
    }

    public static class SendChannelEndpointInterceptor
    {
        public static class Presend
        {
            @Advice.OnMethodEnter
            public static void presend(final ByteBuffer buffer, final InetSocketAddress address)
            {
                LOGGER.logFrameOut(buffer, address);
            }
        }

        public static class Dispatch
        {
            @Advice.OnMethodEnter
            public static void dispatch(final UnsafeBuffer buffer, final int length, final InetSocketAddress srcAddress)
            {
                LOGGER.logFrameIn(buffer, 0, length, srcAddress);
            }
        }
    }

    public static class ReceiveChannelEndpointInterceptor
    {
        public static class SendTo
        {
            @Advice.OnMethodEnter
            public static void sendTo(final ByteBuffer buffer, final InetSocketAddress address)
            {
                LOGGER.logFrameOut(buffer, address);
            }
        }

        public static class Dispatch
        {
            @Advice.OnMethodEnter
            public static void dispatch(final UnsafeBuffer buffer, final int length, final InetSocketAddress srcAddress)
            {
                LOGGER.logFrameIn(buffer, 0, length, srcAddress);
            }
        }
    }
}

package cn.gamemate.app.cassandra;

import java.nio.ByteBuffer;

import org.safehaus.uuid.UUID;

import me.prettyprint.cassandra.serializers.AbstractSerializer;

public class JugUuidSerializer extends AbstractSerializer<UUID> {
	private static final JugUuidSerializer instance = new JugUuidSerializer();

	public static JugUuidSerializer get() {
		return instance;
	}

	@Override
	public ByteBuffer toByteBuffer(UUID obj) {
		return ByteBuffer.wrap(obj.asByteArray());
	}

	@Override
	public UUID fromByteBuffer(ByteBuffer bytes) {
		if (bytes == null) {
			return null;
		}
		ByteBuffer buffer = ByteBuffer.allocate(16);
		buffer.putLong(bytes.getLong());
		buffer.putLong(bytes.getLong());
		return new UUID(buffer.array());
	}

}

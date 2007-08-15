package games.stendhal.server.tools;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import games.stendhal.tools.tiled.LayerDefinition;
import games.stendhal.tools.tiled.StendhalMapStructure;
import games.stendhal.tools.tiled.TileSetDefinition;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import marauroa.common.net.InputSerializer;
import marauroa.common.net.OutputSerializer;

import org.junit.Before;
import org.junit.Test;

public class LayerDefinitionTest {
	StendhalMapStructure map;

	@Before
	public void setUp() {
		map = new StendhalMapStructure(64, 64);
		TileSetDefinition set = null;

		set = new TileSetDefinition("name1", 1);
		set.setSource("source1");
		map.addTileset(set);

		set = new TileSetDefinition("name2", 10);
		set.setSource("source2");
		map.addTileset(set);

		set = new TileSetDefinition("name3", 55);
		set.setSource("source3");
		map.addTileset(set);

		set = new TileSetDefinition("name4", 100);
		set.setSource("source4");
		map.addTileset(set);

		LayerDefinition layer = null;
		layer = new LayerDefinition(64, 64);
		layer.build();

		layer.setName("layer1");
		layer.set(10, 20, 1);
		layer.set(19, 7, 10);
		layer.set(11, 2, 120);
		layer.set(15, 21, 64);
		map.addLayer(layer);
	}

	@Test
	public void testBelongToTileset() {
		LayerDefinition layer = map.getLayer("layer1");
		assertNotNull(layer);

		int tileid = layer.getTileAt(10, 20);
		assertEquals(1, tileid);

		assertEquals("source1", layer.getTilesetFor(layer.getTileAt(10, 20))
				.getSource());
		assertEquals("source2", layer.getTilesetFor(layer.getTileAt(19, 7))
				.getSource());
		assertEquals("source4", layer.getTilesetFor(layer.getTileAt(11, 2))
				.getSource());
		assertEquals("source3", layer.getTilesetFor(layer.getTileAt(15, 21))
				.getSource());

		assertEquals(0, layer.getTileAt(57, 34));
		assertNull(layer.getTilesetFor(layer.getTileAt(57, 34)));
	}

	@Test
	public void testSerialization() throws IOException, ClassNotFoundException {
		ByteArrayOutputStream array = new ByteArrayOutputStream();
		OutputSerializer out = new OutputSerializer(array);

		LayerDefinition layer = map.getLayer("layer1");
		layer.writeObject(out);

		byte[] serialized = array.toByteArray();

		ByteArrayInputStream sarray = new ByteArrayInputStream(serialized);
		InputSerializer in = new InputSerializer(sarray);

		LayerDefinition serializedLayer = (LayerDefinition) in
				.readObject(new LayerDefinition(0, 0));

		assertEquals(layer.getName(), serializedLayer.getName());
		assertEquals(layer.getWidth(), serializedLayer.getWidth());
		assertEquals(layer.getHeight(), serializedLayer.getHeight());
		assertEquals(layer.exposeRaw().length,
				serializedLayer.exposeRaw().length);

		byte[] a = layer.exposeRaw();
		byte[] b = serializedLayer.exposeRaw();

		for (int i = 0; i < a.length; i++) {
			assertEquals(a[i], b[i]);
		}
	}

}

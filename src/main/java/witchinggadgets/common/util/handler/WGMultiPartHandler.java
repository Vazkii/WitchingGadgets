package witchinggadgets.common.util.handler;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.tiles.TileTube;
import thaumcraft.common.tiles.TileTubeBuffer;
import thaumcraft.common.tiles.TileTubeFilter;
import thaumcraft.common.tiles.TileTubeValve;
import witchinggadgets.common.blocks.tiles.MultipartEssentiaBuffer;
import witchinggadgets.common.blocks.tiles.MultipartEssentiaTube;
import witchinggadgets.common.blocks.tiles.MultipartEssentiaTube_Filtered;
import witchinggadgets.common.blocks.tiles.MultipartEssentiaTube_Valve;
import codechicken.lib.raytracer.RayTracer;
import codechicken.lib.vec.BlockCoord;
import codechicken.multipart.MultiPartRegistry;
import codechicken.multipart.MultiPartRegistry.IPartConverter;
import codechicken.multipart.MultiPartRegistry.IPartFactory;
import codechicken.multipart.MultipartGenerator;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;

public class WGMultiPartHandler implements IPartFactory, IPartConverter
{
	public static WGMultiPartHandler instance = new WGMultiPartHandler();
	public static RayTracer rayTracer = new RayTracer();

	public void init()
	{
		MultiPartRegistry.registerConverter(this);
		MultiPartRegistry.registerParts(this, new String[] {
//				"witchingGadgets:vis_relay",
				"witchingGadgets:essentia_tube",
				"witchingGadgets:essentia_tube_valve",
				"witchingGadgets:essentia_tube_filtered",
				"witchingGadgets:essentia_buffer"});
		MultipartGenerator.registerPassThroughInterface("thaumcraft.api.aspects.IEssentiaTransport",true,true);
		MultipartGenerator.registerPassThroughInterface("thaumcraft.api.wands.IWandable",true,true);
	}

	@Override
	public Iterable<Block> blockTypes()
	{
		Set<Block> set = new HashSet();
//		set.add(WGContent.BlockMPVisRelay);
		set.add(ConfigBlocks.blockTube);
		return set;
	}

	@Override
	public TMultiPart convert(World world, BlockCoord coord)
	{
		Block b = world.getBlock(coord.x, coord.y, coord.z);
		int meta = world.getBlockMetadata(coord.x, coord.y, coord.z);
//		if (b.equals(WGContent.BlockMPVisRelay))
//		{
//			//			System.out.println(world.getTileEntity(coord.x, coord.y, coord.z));
//			//			System.out.println("conversion!");
//			return new MultipartVisRelay(meta);
//		}
		if (b.equals(ConfigBlocks.blockTube))
		{
			if(world.getTileEntity(coord.x, coord.y, coord.z) instanceof TileTubeValve)
				return new MultipartEssentiaTube_Valve(meta);
			else if(world.getTileEntity(coord.x, coord.y, coord.z) instanceof TileTubeFilter)
				return new MultipartEssentiaTube_Filtered(meta);
			else if(world.getTileEntity(coord.x, coord.y, coord.z) instanceof TileTubeBuffer)
				return new MultipartEssentiaBuffer(meta);
			else
				return new MultipartEssentiaTube(meta);
		}
		return null;
	}

	@Override
	public TMultiPart createPart(String name, boolean client)
	{
//		if (name.equals("witchingGadgets:vis_relay"))
//			return new MultipartVisRelay(0);
		if (name.equals("witchingGadgets:essentia_tube"))
			return new MultipartEssentiaTube(0);
		if (name.equals("witchingGadgets:essentia_tube_valve"))
			return new MultipartEssentiaTube_Valve(0);
		if (name.equals("witchingGadgets:essentia_tube_filtered"))
			return new MultipartEssentiaTube_Filtered(0);
		if (name.equals("witchingGadgets:essentia_buffer"))
			return new MultipartEssentiaBuffer(0);
		return null;
	}
	
	public static boolean tileIsEssentiaTube(TileEntity tile)
	{
		if(tile instanceof TileTube)
			return true;
		if(tile instanceof TileMultipart)
			for(TMultiPart part : ((TileMultipart)tile).jPartList())
				if(part instanceof MultipartEssentiaTube)
					return true;
		return false;
	}

}
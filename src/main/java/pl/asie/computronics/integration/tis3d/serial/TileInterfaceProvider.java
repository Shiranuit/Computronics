package pl.asie.computronics.integration.tis3d.serial;

import li.cil.tis3d.api.serial.SerialInterface;
import li.cil.tis3d.api.serial.SerialInterfaceProvider;
import li.cil.tis3d.api.serial.SerialProtocolDocumentationReference;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * @author Sangar, Vexatos
 */
public abstract class TileInterfaceProvider implements SerialInterfaceProvider {

	protected final Class<?> tileClass;
	protected final String name, link;

	public TileInterfaceProvider(Class<?> tileClass, String name, String link) {
		this.tileClass = tileClass;
		this.name = name;
		this.link = link;
	}

	@Override
	public boolean worksWith(World world, BlockPos pos, EnumFacing side) {
		if(tileClass == null) {
			// This can happen if filter classes are deduced by reflection and
			// the class in question is not present.
			return false;
		}
		final TileEntity tile = world.getTileEntity(pos);
		return tile != null && tileClass.isInstance(tile);
	}

	@Override
	public SerialProtocolDocumentationReference getDocumentationReference() {
		return new SerialProtocolDocumentationReference("tooltip.computronics.manual.tis3d.port." + name, "protocols/computronics/" + link);
	}

	protected abstract boolean isStillValid(World world, BlockPos pos, EnumFacing side, SerialInterface serialInterface, TileEntity tile);

	@Override
	public boolean isValid(World world, BlockPos pos, EnumFacing side, SerialInterface serialInterface) {
		if(tileClass == null) {
			// This can happen if filter classes are deduced by reflection and
			// the class in question is not present.
			return false;
		}
		final TileEntity tile = world.getTileEntity(pos);
		return tile != null && isStillValid(world, pos, side, serialInterface, tile);
	}
}
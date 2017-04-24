package zmaster587.advancedRocketry.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import zmaster587.advancedRocketry.api.dimension.solar.StellarBody;
import zmaster587.advancedRocketry.dimension.DimensionManager;
import zmaster587.advancedRocketry.tile.station.TilePlanetaryHologram;

public class EntityUIStar extends EntityUIPlanet {
	
	StellarBody star;
	public final static int starIDoffset = 10000;
	
	public EntityUIStar(World worldIn, StellarBody properties, TilePlanetaryHologram tile, double x, double y, double z) {
		this(worldIn);
		setPosition(x, y, z);
		setProperties(properties);
		this.tile = tile;
	}
	
	public EntityUIStar(World worldIn) {
		super(worldIn);
		setSize(0.2f, 0.2f);
	}
	
	public void setProperties(StellarBody properties) {
		this.star = properties;
		if(properties != null)
			this.dataManager.set(planetID, star.getId());
		else
			this.dataManager.set(planetID, -1);
	}
	
	public int getPlanetID() {
		//this.dataManager.set(planetID, 256);

		if(!worldObj.isRemote)
			return star == null ? -1 : star.getId();

		int planetId = this.dataManager.get(planetID);

		if(star != null && star.getId() != planetId) {
			if(planetId == -1 )
				star = null;
			else
				star = DimensionManager.getInstance().getStar(planetId);
		}

		return this.dataManager.get(planetID);
	}
	
	public StellarBody getStarProperties() {
		if((star == null && getPlanetID() != -1) || (star != null && getPlanetID() != star.getId())) {
			star = DimensionManager.getInstance().getStar(getPlanetID());
		}

		return star;
	}
	
	@Override
	public boolean processInitialInteract(EntityPlayer player, ItemStack stack,
			EnumHand hand) {
		if(!worldObj.isRemote && tile != null) {
			tile.selectSystem(star.getId() + starIDoffset);
		}
		return true;
	}
}
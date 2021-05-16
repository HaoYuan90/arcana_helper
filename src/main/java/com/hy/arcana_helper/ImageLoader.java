package com.hy.arcana_helper;

import lombok.Builder;
import lombok.Data;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ImageLoader {
	private static final List<FIndexSectionConfig> F_INDEX_SECTION_CONFIGS;

	// TODO : Validate image row/column against findex
	static {
		F_INDEX_SECTION_CONFIGS = List.of(
				// 1 star
			FIndexSectionConfig.builder()
					.sectionName("1-star")
					.imageConfig(
							FIndexImageConfig.builder()
									.xOffset(130)
									.yOffset(103)
									.resourcePath("/img1.png")
									.build()
					)
					.championConfig(
							FIndexChampionConfig.builder()
									.champions(
											List.of(
													List.of(
															new FIndexChampion("swordsman"),
															new FIndexChampion("archer"),
															new FIndexChampion("rogue"),
															new FIndexChampion("wizard"),
															new FIndexChampion("cleric"),
															new FIndexChampion("warrior"),
															new FIndexChampion("lancer"),
															new FIndexChampion("spirit")
													)
											)
									)
									.build()
					)
					.build()
				// 2 star
		);
	}

	private static final int IMAGE_WIDTH = 174;
	private static final int IMAGE_X_MARGIN = 2;
	private static final int IMAGE_LENGTH = 77;
	private static final int IMAGE_Y_MARGIN = 1;

	public static void main(String[] args) throws IOException {
		String dir = "output";
		Path outputFolder = Paths.get(dir);
		Files.createDirectories(outputFolder);

		for (FIndexSectionConfig sectionConfig : F_INDEX_SECTION_CONFIGS) {
			Path subOutputFolder = Paths.get(dir + "/" + sectionConfig.getSectionName());
			Files.createDirectories(subOutputFolder);

			FIndexImageConfig imageConfig = sectionConfig.getImageConfig();
			FIndexChampionConfig championConfig = sectionConfig.getChampionConfig();

			BufferedImage image = ImageIO.read(ImageLoader.class.getResource(imageConfig.getResourcePath()));

			for (int column = 0; column < championConfig.getChampions().size(); column ++) {
				List<FIndexChampion> championsInColumn = championConfig.getChampions().get(column);
				for (int row = 0; row < championsInColumn.size(); row ++) {
					System.out.println(row);
					FIndexChampion champion = championsInColumn.get(row);
					BufferedImage subImage = image.getSubimage(
							imageConfig.getXOffset() + column * (IMAGE_WIDTH + IMAGE_X_MARGIN),
							imageConfig.getYOffset() + row * (IMAGE_LENGTH + IMAGE_Y_MARGIN),
							IMAGE_WIDTH,
							IMAGE_LENGTH
					);
					ImageIO.write(subImage, "png",
							new File(Paths.get(subOutputFolder.toString(), champion.getName() + ".png").toString()));
				}
			}
		}
	}

	@Data
	@Builder
	public static class FIndexSectionConfig {
		private final String sectionName;
		private final FIndexImageConfig imageConfig;
		private final FIndexChampionConfig championConfig;

		public boolean isValid() {
			// TODO : validate row/column against champion list
			return true;
		}
	}

	@Data
	@Builder
	public static class FIndexImageConfig {
		private final int xOffset;
		private final int yOffset;
		private final String resourcePath;
	}

	@Data
	@Builder
	public static class FIndexChampionConfig {
		private final List<List<FIndexChampion>> champions; // list of columns (mostly lists of lists of size 8)
	}

	@Data
	public static class FIndexChampion {
		private final String name;
	}
}

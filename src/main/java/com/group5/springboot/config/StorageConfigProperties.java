package com.group5.springboot.config;

import java.nio.file.Path;
import java.util.Arrays;

public abstract class StorageConfigProperties {
	private Path root;

	public static final String UPLOAD_NODE = "uploads";
	public static final String MEDIA_URL_BASE = "user-media";


	@SuppressWarnings("unused")
	Path getRoot() {
		return root;
	}

	@SuppressWarnings("unused")
	void setRoot(Path root) {
		this.root = root;
	}

	public String getRootAbs() {
		return root.toAbsolutePath().normalize().toString();
	}

	public String getEventImageUploadStorageDir() {
		return join("/", getRootAbs(), UPLOAD_NODE, "event", "images");
	}

	public String getProductImageUploadStorageDir() {
		return join("/", getRootAbs(), UPLOAD_NODE, "product", "images");
	}

	public String getProductVideoUploadStorageDir() {
		return join("/", getRootAbs(), UPLOAD_NODE, "product", "videos");
	}

	// fixme: inconsistent naming convention
	public String getQuestionAudioAndImageUploadStorageDir() {
		return join("/", getRootAbs(), UPLOAD_NODE, "question", "audios-and-images");
	}

	public String getUserAvatarUploadStorageDir() {
		return join("/", getRootAbs(), UPLOAD_NODE, "user", "avatars");
	}

	// fixme: move to storageService and use Path to resolve path safely
	public static String storagePathToViewAndDbUrl(String storagePath) {
		String replacedAfter = UPLOAD_NODE + "/";
		return MEDIA_URL_BASE
			   + "/"
			   + storagePath.substring(storagePath.lastIndexOf(replacedAfter) + replacedAfter.length());
	}


	// helpers
	/** Remove potential additional slash(/) from root */
	private static String join(String... parts) {
		return String.join("/", Arrays.stream(parts)
				.map(p -> p.replaceAll("/+$", ""))
				.toArray(String[]::new));
	}
}
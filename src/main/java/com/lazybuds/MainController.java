package com.lazybuds;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lazybuds.models.Image;

@Controller
public class MainController {

	@Value("${FILE_BASE_DIR}")
	private String FILE_BASE_DIR;

	@RequestMapping(value = "/ping", method = RequestMethod.GET)
	public @ResponseBody String health() {
		return "pong";
	}

	@RequestMapping(value = "/session/{sessionId}/image", method = RequestMethod.POST)
	public @ResponseBody String addImage(@PathVariable("sessionId") String sessionId,
			@RequestParam("index") String index, @RequestParam("base64Image") String base64Image) {

		byte[] bytes = Base64.getDecoder().decode(base64Image);
		Path path = Paths.get(FILE_BASE_DIR + "/" + sessionId);
		if (!path.toFile().exists()) {
			path.toFile().mkdir();
		}

		File file = new File(FILE_BASE_DIR + "/" + sessionId + "/" + index + ".jpg");

		try (OutputStream os = new FileOutputStream(file);) {
			os.write(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return index;
	}

	@RequestMapping(value = "/session/{sessionId}/text", method = RequestMethod.POST)
	public @ResponseBody String addText(@PathVariable("sessionId") String sessionId,
			@RequestParam("index") String index, @RequestParam("text") String text) {

		Path path = Paths.get(FILE_BASE_DIR + "/" + sessionId);
		if (!path.toFile().exists()) {
			path.toFile().mkdir();
		}

		File file = new File(FILE_BASE_DIR + "/" + sessionId + "/" + index + ".txt");

		try (OutputStream os = new FileOutputStream(file);) {
			os.write(text.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return index;
	}

	@RequestMapping(value = "/session/{sessionId}/image/{index}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
	public @ResponseBody byte[] getImage(@PathVariable("sessionId") String sessionId, @PathVariable("index") String index,
			HttpServletResponse response) throws IOException {
		File file = new File(FILE_BASE_DIR + "/" + sessionId + "/" + index + ".jpg");
		return FileUtils.readFileToByteArray(file);
	}

	@RequestMapping(value = "/session/{sessionId}", method = RequestMethod.GET)
	public ModelAndView displayData(@PathVariable("sessionId") String sessionId) throws IOException {

		Path path = Paths.get(FILE_BASE_DIR + "/" + sessionId);
		List<Image> images = new ArrayList<Image>();
		if (path.toFile().exists()) {
			System.out.println("Folder exists");
			File[] files = path.toFile().listFiles((dir, filename) -> filename.endsWith("jpg"));
			System.out.println("Image count:" + files.length);
			for (int i = 1; i <= files.length; i++) {
				File file = new File(FILE_BASE_DIR + "/" + sessionId + "/" + i + ".txt");
				String text = null;
				if (file.exists()) {
					text = FileUtils.readFileToString(file);
				}
				images.add(new Image(sessionId+"/image/"+i, text));
			}
		}
		ModelAndView map = new ModelAndView("display");
		System.out.println(images);
		map.addObject("images", images);
		return map;
	}
}

package com.secondshops.services;

import com.secondshops.mappers.ImageMapper;
import com.secondshops.models.Image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ImageService {
	@Autowired
	private ImageMapper imageMapper;

	@Transactional
	public List<Image> getImageByGoodId(Integer goodId) {
		return imageMapper.getImageByGoodId(goodId);
	}

	@Transactional
	public int insertImage(Image image) {
		return imageMapper.insertImage(image);
	}

	@Transactional
	public int deleteImage(Integer goodId) {
		return imageMapper.deleteImage(goodId);
	}
}

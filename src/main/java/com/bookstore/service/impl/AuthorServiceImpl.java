package com.bookstore.service.impl;

import com.bookstore.domain.Author;
import com.bookstore.domain.ImageAuthor;
import com.bookstore.repository.AuthorRepository;
import com.bookstore.repository.ImageAuthorRepository;
import com.bookstore.service.AuthorService;
import com.bookstore.service.dto.AuthorDTO;
import com.bookstore.service.mapper.AuthorMapper;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bookstore.domain.Author}.
 */
@Service
@Transactional
public class AuthorServiceImpl implements AuthorService {

    private static final Logger LOG = LoggerFactory.getLogger(AuthorServiceImpl.class);

    private final AuthorRepository authorRepository;
    private final ImageAuthorRepository imageAuthorRepository;

    private final AuthorMapper authorMapper;

    public AuthorServiceImpl(AuthorRepository authorRepository, ImageAuthorRepository imageAuthorRepository, AuthorMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.imageAuthorRepository = imageAuthorRepository;
        this.authorMapper = authorMapper;
    }

    @Override
    public AuthorDTO save(AuthorDTO authorDTO, String contextPath) {
        LOG.debug("Request to save Author : {}", authorDTO);

        // Chuyển đổi DTO thành thực thể Author
        Author author = authorMapper.toEntity(authorDTO);

        // Lưu tác giả vào cơ sở dữ liệu
        author = authorRepository.save(author);

        // Kiểm tra xem có hình ảnh nào không
        if (authorDTO.getImage() != null && !authorDTO.getImage().isEmpty()) {
            try {
                // Đường dẫn lưu trữ hình ảnh
                String filePath = contextPath + "/content/img/authors/" + authorDTO.getImage().getOriginalFilename();

                LOG.debug("Saving image : {}", filePath);
                // Lưu hình ảnh vào hệ thống tệp
                Files.write(Path.of(filePath), authorDTO.getImage().getBytes());

                // Lưu thông tin hình ảnh vào cơ sở dữ liệu
                ImageAuthor imageAuthor = new ImageAuthor();
                imageAuthor.setAuthor(author); // Liên kết hình ảnh với tác giả
                imageAuthor.setImage_url(authorDTO.getImage().getOriginalFilename()); // Đặt đường dẫn hình ảnh
                imageAuthorRepository.save(imageAuthor); // Lưu hình ảnh vào cơ sở dữ liệu
            } catch (IOException e) {
                LOG.error("Error while saving image file: {}", e.getMessage());
                throw new RuntimeException("Failed to save image file", e); // Ném ngoại lệ nếu có lỗi
            }
        }

        // Chuyển đổi thực thể Author thành AuthorDTO và trả về
        return authorMapper.toDto(author);
    }

    @Override
    public AuthorDTO update(AuthorDTO authorDTO, String contextPath) {
        LOG.debug("Request to update Author : {}", authorDTO);
        Author author = authorMapper.toEntity(authorDTO);
        author = authorRepository.save(author);

        var imageAuthorList = imageAuthorRepository.findByAuthorId(author.getId());

        if (imageAuthorList != null && imageAuthorList.size() > 0) {
            // Cập nhật ảnh đầu tiên
            var firstImage = imageAuthorList.get(0);

            // Xóa file ảnh cũ
            var oldImageFile = firstImage.getImage_url();
            var oldImageFilePath = contextPath + "/content/img/authors/" + oldImageFile;
            File oldFile = new File(oldImageFilePath);
            if (oldFile.exists()) {
                oldFile.delete();
                LOG.info("Deleted old image {}", oldImageFilePath);
            } else {
                LOG.warn("Old image not found at {}", oldImageFilePath);
            }

            // Lưu file ảnh mới
            String newFilePath = contextPath + "/content/img/authors/" + authorDTO.getImage().getOriginalFilename();
            try {
                Files.write(Path.of(newFilePath), authorDTO.getImage().getBytes());
            } catch (IOException e) {
                LOG.error("Error while writing new image to file system", e);
                throw new RuntimeException("Error saving the new image", e);
            }

            // Cập nhật URL của ảnh trong cơ sở dữ liệu
            firstImage.setImage_url(authorDTO.getImage().getOriginalFilename());
            imageAuthorRepository.save(firstImage);
        } else {
            String fileName = authorDTO.getImage().getOriginalFilename();
            LOG.info("New image filename: {}", fileName);

            String filePath = contextPath + "/content/img/authors/" + fileName;
            try {
                Files.write(Path.of(filePath), authorDTO.getImage().getBytes());
                LOG.info("Saved new image at path: {}", filePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Tạo đối tượng ImageAuthor mới để lưu trữ thông tin ảnh
            ImageAuthor newImage = new ImageAuthor();
            newImage.setAuthor(author);
            newImage.setImage_url(fileName);

            // Lưu thông tin ảnh mới vào cơ sở dữ liệu
            ImageAuthor savedImage = imageAuthorRepository.save(newImage);
            LOG.info("New image saved with URL: {}", savedImage.getImage_url());
        }
        return authorMapper.toDto(author);
    }

    @Override
    public Optional<AuthorDTO> partialUpdate(AuthorDTO authorDTO) {
        LOG.debug("Request to partially update Author : {}", authorDTO);

        return authorRepository
            .findById(authorDTO.getId())
            .map(existingAuthor -> {
                authorMapper.partialUpdate(existingAuthor, authorDTO);

                return existingAuthor;
            })
            .map(authorRepository::save)
            .map(authorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AuthorDTO> findOne(Long id) {
        LOG.debug("Request to get Author : {}", id);
        return authorRepository.findById(id).map(authorMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Author : {}", id);
        authorRepository.deleteById(id);
    }
}

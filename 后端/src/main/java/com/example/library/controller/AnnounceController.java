package com.example.library.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.library.beans.Announce;
import com.example.library.mapper.AnnounceMapper;
import com.example.library.pojo.AnnounceMoudle;
import com.example.library.pojo.AnnounceWithPage;

@RestController
public class AnnounceController {
	@Resource
	private AnnounceMapper announcemapper;

	@PostMapping("/announce")
	public void currentBooks(@RequestBody Announce announce) {
		announcemapper.insert(announce);
	}

	@PostMapping("/select/lastannounce")
	public Announce selectLastAnnounce() {
		List<Announce> list = announcemapper.selectAnnounces();
		if (list.size() - 1<0) {
			Announce announce = new Announce();
			announce.setConnent("暂无公告");
			return announce;
		}
		return list.get(list.size() - 1);
	}

	@PostMapping("/selectannounce")
	public AnnounceMoudle selectAnnounces(@RequestBody AnnounceWithPage announceWithPage) {
		announceWithPage.setStaIndex((announceWithPage.getPageNumber() - 1) * 3);
		AnnounceMoudle announceMoudle = new AnnounceMoudle();
		announceMoudle.setList(announcemapper.selectAnnounceWithPages(announceWithPage));
		announceMoudle.setTotal(announcemapper.selectCountAnnounce());
		return announceMoudle;
	}

}

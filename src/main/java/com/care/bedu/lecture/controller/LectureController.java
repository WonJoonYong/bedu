package com.care.bedu.lecture.controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.care.bedu.lecture.service.LectureService;
import com.care.bedu.lecture.vo.LectureDetailVO;
import com.care.bedu.lecture.vo.LectureVO;

@RestController
@RequestMapping("/api/lect")
public class LectureController {
	
	@Autowired
	private LectureService LectureService;

	/* 강의 리스트 조회 */
	@RequestMapping("/lectureList")
	public HashMap<String, Object> getLectureList(String category){
		ArrayList<Object> list = new ArrayList<>();
		HashMap<String, Object> map = new HashMap<>();
		list = LectureService.getLectureList(category);
		
		map.put("item", list);
		return map;
	}
	
	/* 강의 상세정보 조회 */
	@RequestMapping("/lectureDetail")
	public LectureVO getLectureDetail(int num) {
		LectureVO dto = new LectureVO();
		
		dto = LectureService.getLectureDetail(num);
		
		return dto;
	}

	/* 신규 오픈 강좌 4개 조회 */
	@RequestMapping("/getNewestLecture")
	public HashMap<String, Object> getNewestLecture(){
		HashMap<String, Object> map = new HashMap<>();

		map = LectureService.getNewestLecture();

		return map;
	}
	
	
	/* 강의 동영상 목록 조회 */
	@RequestMapping("/getVideoList")
	public ArrayList<LectureDetailVO> getVideoList(int num){
		ArrayList<LectureDetailVO> list = new ArrayList<>();
		
		list = LectureService.getVideoList(num);
		
		return list;
	}

	/* 검색화면 조회 */
	@RequestMapping("/lectureSearch")
	public HashMap<String,ArrayList<LectureVO>> lectureSearch(String keyword, int page){
		HashMap<String, ArrayList<LectureVO>> map = new HashMap<>();
		
		map = LectureService.lectureSearch(keyword, page);
		
		return map;
	}

	/* 검색화면 조회시 토탈 갯수 */
	@RequestMapping("/searchTotal")
	public int searchTotal(String keyword){
		int total = LectureService.searchTotal(keyword);
		return total;
	}

	/* 강의 후기 조회 */
	

}

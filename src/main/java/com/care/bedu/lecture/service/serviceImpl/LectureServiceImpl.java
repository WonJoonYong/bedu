package com.care.bedu.lecture.service.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.care.bedu.lecture.dao.LectureCategoryDAO;
import com.care.bedu.lecture.dao.LectureDAO;
import com.care.bedu.lecture.service.LectureService;
import com.care.bedu.lecture.vo.LectureCategoriesVO;
import com.care.bedu.lecture.vo.LectureDetailVO;
import com.care.bedu.lecture.vo.LectureVO;
import com.care.bedu.review.vo.ReviewVO;


@Service
public class LectureServiceImpl implements LectureService{

	@Autowired
	private LectureDAO lectureDao;

	@Autowired
	private LectureCategoryDAO cateDao;

	
	/* 카테고리별 강의를 들어갔을 때 동영상 조회 */
	@Override
	public ArrayList<Object>  getLectureList(String category) {
		/* 중분류에 따른 소분류 조회 -> 소분류에 따른 강의 목록 조회
		 * -> 자료 구조 정리하여 반환
		 */
		ArrayList<LectureCategoriesVO> dtos = new ArrayList<>(); // 소분류 데이터를 저장할 빈 배열 생성
		dtos = cateDao.getBot(category); // 소분류 카테고리 조회

		HashMap<String, Object> map = new HashMap<>(); // 파라미터를 담을 해쉬맵

		ArrayList<LectureVO> list = new ArrayList<>(); // 강의 목록을 담을 배열

		ArrayList<Object> result = new ArrayList<>(); // 반환 값을 담을 배열
		

		/* 반복문 사용하여 데이터 구조화 */
		for(LectureCategoriesVO dto : dtos){
			HashMap<String,Object> lect = new HashMap<>(); // 구조화된 데이터를 담을 해쉬맵 생성 
			map.put("category", dto.getCateCode()); 
			list = lectureDao.getLectureList(map); // 소분류에 따른 강의 목록 조회
			lect.put("cateCode", dto.getCateCode()); // 소분류 코드
			lect.put("cateKor", dto.getCateKor()); // 소분류 한글
			lect.put("item",list); // 소분류 강의 목록
			result.add(lect);
		}

		return result;
	}



	/* 강의 상세 정보 조회 */
	@Override
	public LectureVO getLectureDetail(int num) {
		LectureVO dto = new LectureVO();
		dto = lectureDao.getLectureDetail(num);
		
		return dto;
	}

	/* 해당 강의에 포함되어있는 커리큘럼(동영상 목록)을 조회 */
	@Override
	public ArrayList<LectureDetailVO> getVideoList(int num) {
		ArrayList<LectureDetailVO> list = new ArrayList<>();
		
		list = lectureDao.getVideoList(num);
		
		return list;
	}

	/* 검색을 통한 결과 조회 */
	@Override
	public HashMap<String, ArrayList<LectureVO>> lectureSearch(String keyword, int page) {
		HashMap<String, ArrayList<LectureVO>> map = new HashMap<>();
		ArrayList<LectureVO> dto = new ArrayList<>();

		HashMap<String, Object> arg = new HashMap<>(); // 파라미터 맵 생성
		arg.put("keyword", keyword); // 검색 키워드
		arg.put("begin", (page-1)*5); // 시작 인덱스 설정
		dto = lectureDao.lectureSearch(arg);
		map.put("item", dto);


		return map;
	}

	/* 검색했을때 해당 키워드로 조회되는 총 강의 수를 확인 */
	@Override
	public int searchTotal(String keyword) {
		int result = 0;
		result = lectureDao.searchTotal(keyword);
		return result;
	}

	/* 내가 좋아요한 강의 목록 조회 */
	@Override
	public ArrayList<Integer> getLikeList(String userId) {

		ArrayList<Integer> list = new ArrayList<>();

		list = lectureDao.getLikeList(userId);

		return list;
	}


	/* 내가 좋아요한 목록을 조회하고 likeYn 컬럼에 값 넣기 위한 메서드 
	 * 좋아요 기능의 구현을 어떻게 할지 명확하지 않아 사용 여부는 좀더 고려해볼것
	*/
	public ArrayList<LectureVO> likeCheck(ArrayList<LectureVO> dto){
		ArrayList<Integer> likes = new ArrayList<>(); // 좋아요한 게시글 고유번호를 담을 빈 배열
		likes = lectureDao.getLikeList("123"); // 사용자 ID를 기준으로 데이터 조회 지금은 임시로 "123"을 넣었지만 나중에는 ID로 변경할 예정
		for(int i = 0; i< dto.size();i++){  // 조회한 게시글 목록을 반복문 사용하여 체크
			/* 좋아요한 게시글 목록에 해당 게시글 번호가 포함 여부에 따라 값 체크 */
			if(likes.contains(dto.get(i).getLectNum())){ 
				dto.get(i).setLikeYn(1);
			} else {
				dto.get(i).setLikeYn(0);
			}
		}
		return dto;
	}



	/* 신규 오픈 강좌 4개 조회 */
	@Override
	public HashMap<String, Object> getNewestLecture() {
		HashMap<String, Object> map = new HashMap<>();
		ArrayList<LectureVO> list = new ArrayList<>();

		list = lectureDao.getNewestLecture();

		map.put("item", list);
		return map;
	}



	/* 후기 조회
	 * 아직까지 후기 조회의 기준이 없어서 임시로 최신순으로 조회하도록 구현
	 */
	@Override
	public HashMap<String, Object> getReview(int num) {
		HashMap<String, Object> map = new HashMap<>();

		ArrayList<ReviewVO> result = new ArrayList<>();

		result = lectureDao.getReview(num);

		map.put("item", result);

		return map;
	}



	@Override
	public int addToCart(int lectNum, int userNum) {
		int result = 0;

		/* 파라미터를 담을 맵 */
		HashMap<String, Object> arg = new HashMap<>();

		/* 강의 고유번호를 기준으로 데이터 조회 */
		LectureVO lect = lectureDao.getLectureDetail(lectNum);


		// /* 파라미터 맵에 저장 */
		arg.put("lectNum", lectNum);
		arg.put("userNum", userNum);
		arg.put("title",lect.getTitle());
		arg.put("teacher",lect.getTeacher());
		arg.put("summary", lect.getLectSum());
		arg.put("thumbnail", lect.getThumbnail());
		arg.put("price", Integer.parseInt(lect.getPrice().replace(",", "")));

		/* 장바구니에 추가 */
		result = lectureDao.addToCart(arg);


		return result;
	
	}



	@Override
	public HashMap<String, Object> getCart(int num) {
		HashMap<String, Object> result = new HashMap<>();

		ArrayList<LectureVO> carts = new ArrayList<>();

		carts = lectureDao.getCart(num);

		result.put("item", carts);

		return result;
	}
	
}

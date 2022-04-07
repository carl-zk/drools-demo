package com.domain.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.domain.dao.KieHisMapper;
import com.domain.entity.KieHis;
import com.domain.service.KieHisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author carl
 */
@Slf4j
@Service
public class KieHisServiceImpl extends ServiceImpl<KieHisMapper, KieHis> implements KieHisService {
}
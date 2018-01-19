package com.wizinno.livgo.app.repository;

import com.wizinno.livgo.app.document.Live;
import com.wizinno.livgo.app.repository.enhance.CommonRepository;

/**
 * Created by LiuMei on 2017-05-19.
 */
public interface LiveRepository extends CommonRepository<Live, String> {
 Live findById(Long liveId);
}

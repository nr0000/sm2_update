package com.ssh.sm2_update.service.qtService;

import com.ssh.sm2_update.bean.qtBean.*;

public interface GetResourceFromQtService {

    QtVodCategoryPage getAllVodCategories();

    QtVodAlbumPage getVodAlbum(String qtCategoryId, boolean order, int curPage, int pageSize);

    QtVodAlbumDetailPage getVodAlbumDetail(String qtVodAlbumId);

    QtLiveAudioPage getLiveAudio(boolean order, int curPage, int pageSize);

    QtLProgramPage getLiveProgram(String channelid);

    QtVodAudioPage getVodAudio(String qtVodAlbumId, boolean order, int curPage, int pageSize);
}

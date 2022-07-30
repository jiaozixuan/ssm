import request from '@/api';

// 新增用户
// 查询用户列表
export function test(query) {
    return request({
        url: '/user',
        method: 'get',
        params: query
    })
}
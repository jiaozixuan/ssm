import store from '@/store'

export default {
    // v-hasPermissions="system:user:add,system:user:query"
    hasPermission: {
        mounted(el, binding) {
            const { value } = binding
            const perms = store.getters && store.getters.perms
            if (value && value instanceof Array && value.length > 0) {
                const hasPermissions = perms.some(perms => {
                    return  value.includes(perms)
                })
                if (!hasPermissions) {
                    // 移除掉当前的element
                    el.parentNode && el.parentNode.removeChild(el)
                }
            } else {
                throw new Error(`请设置操作权限标签值`)
            }
        }
    },
    hasRole: {
        mounted(el, binding) {
            const { value } = binding
            const roles = store.getters && store.getters.roles
            if (value && value instanceof Array && value.length > 0) {
                const hasRoles = roles.some(role => {
                    return value.includes(role)
                })
                if (!hasRoles) {
                    // 移除掉当前的element
                    el.parentNode && el.parentNode.removeChild(el)
                }
            } else {
                throw new Error(`请设置操作权限标签值`)
            }
        }
    }
}
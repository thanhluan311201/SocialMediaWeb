export const KEY_TOKEN = "accessToken";

export const setToken = (token) => {
    localStorage.setItem(KEY_TOKEN, token);
};

export const getToken = () => {
    return localStorage.getItem(KEY_TOKEN);
};

export const removeToken = () => {
    localStorage.removeItem(KEY_TOKEN);
};

export const isTokenExpired = (token) => {
    if (!token) return true;
    const parts = token.split('.');
    if (parts.length !== 3) return true; // Kiểm tra token có đúng định dạng JWT không
    
    try {
        const payload = JSON.parse(atob(parts[1])); // Giải mã payload
        const exp = payload.exp;
        const currentTime = Math.floor(Date.now() / 1000);
        return currentTime > exp;
    } catch (error) {
        console.error("Invalid token:", error);
        return true;
    }
};

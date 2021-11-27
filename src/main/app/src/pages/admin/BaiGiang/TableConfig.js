export const columns = {
    bai_giang: [
        {
            name: 'ID',
            selector: (row) => {
                let id = row?.id;
                if (id) {
                    id = id.substring(id.indexOf('#') + 1)
                }
                
                return (
                    <div>
                        {id}
                    </div>
                )
            },
        },
        {
            name: 'Chương trình',
            selector: row => row?.chuongTrinh,
            sortable: true,
        },
        {
            name: 'Năm học',
            selector: row => row?.namHoc,
            sortable: true,
        },
        {
            name: 'Học kỳ',
            selector: row => row?.hocKy,
            sortable: true,
        },
    ]
}
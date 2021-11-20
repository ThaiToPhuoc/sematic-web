import React, { Component } from 'react'

export default class Accordion extends Component {
    constructor(props) {
        super(props);

        this.state = {
            status: false
        }

        this.handleClick = this.handleClick.bind(this);
    }

    handleClick() {
        this.setState(prev =>({status: !prev.status}))
    }

    render() {
        const {children,title} = this.props;
        return (
            <div>
                <nav className='navbar navbar-expand-md navbar-light bg-secondary text-light mx-auto rounded-2' onClick={this.handleClick}>
                    <div className='mx-5'> 
                        {title}
                    </div>
                </nav>
                {this.state.status && <div>
                    {children}
                </div>}
            </div>
        )
    }
}

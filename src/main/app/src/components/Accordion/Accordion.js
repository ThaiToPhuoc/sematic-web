import React, { Component } from 'react'
import styled from 'styled-components';
import './style.scss'

const CollapseAnim = styled.div`
    max-height: ${props => props.currentHeight};
    transition: all 500ms, border-radius 5s;
    border: ${props => props.currentHeight === '0px' ? '0' : '1'}px solid #ebebeb;
    overflow: hidden;
    text-overflow: ellipsis; 
`

export default class Accordion extends Component {
    constructor(props) {
        super(props);

        this.state = {
            status: false
        }
    }

    handleClick = () => {
        this.setState(prev =>({status: !prev.status}))
    }

    render() {
        const { children, title } = this.props;
        const classNames = `
            navbar navbar-expand-md 
            bg-light border
            mx-auto pointer mt-2
            ${this.state.status ? 'rounded-top' : 'rounded'}
        `
        return (
            <>
                <nav className={classNames}
                    onClick={this.handleClick}> 
                    <div className='px-2 noselect'> 
                        <h5>{title}</h5>
                    </div>
                </nav>
                {
                <CollapseAnim currentHeight={this.state.status ? '600px' : '0px'}>  
                    {children}
                </CollapseAnim>
                }
            </>
        )
    }
}
